//----------------------------------------------------------------------------//
//                                                                            //
//                       F i l a m e n t P a t t e r n                        //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2010. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.sheet.grid;

import java.util.ArrayList;
import java.util.List;

/**
 * Class {@code FilamentPattern} describe a series of y values corresponding to
 * horizontal filaments rather regularly separated
 *
 * @author Hervé Bitteur
 */
public class FilamentPattern
{
    //~ Instance fields --------------------------------------------------------

    /** Column index where sample was taken */
    private final int col;

    /** Series of filaments involved */
    private final List<LineFilament> filaments;

    /** Ordinate value for each filament */
    private final List<Double> ys;

    /** To save processing */
    private boolean processed = false;

    //~ Constructors -----------------------------------------------------------

    //-----------------//
    // FilamentPattern //
    //-----------------//
    /**
     * Creates a new FilamentPattern object.
     * @param col the column index
     */
    public FilamentPattern (int col)
    {
        this.col = col;

        filaments = new ArrayList<LineFilament>();
        ys = new ArrayList<Double>();
    }

    //~ Methods ----------------------------------------------------------------

    //----------//
    // getCount //
    //----------//
    /**
     * Report the number of filaments in this series
     * @return the count
     */
    public int getCount ()
    {
        return filaments.size();
    }

    //-------------//
    // getFilament //
    //-------------//
    public LineFilament getFilament (int index)
    {
        return filaments.get(index);
    }

    //--------------//
    // getFilaments //
    //--------------//
    public List<LineFilament> getFilaments ()
    {
        return filaments;
    }

    //----------//
    // getIndex //
    //----------//
    public int getIndex (LineFilament filament)
    {
        LineFilament ancestor = filament.getAncestor();

        for (int index = 0; index < filaments.size(); index++) {
            LineFilament fil = filaments.get(index);

            if (fil.getAncestor() == ancestor) {
                return index;
            }
        }

        return -1;
    }

    //--------------//
    // setProcessed //
    //--------------//
    /**
     * @param processed the processed to set
     */
    public void setProcessed (boolean processed)
    {
        this.processed = processed;
    }

    //-------------//
    // isProcessed //
    //-------------//
    /**
     * @return the processed
     */
    public boolean isProcessed ()
    {
        return processed;
    }

    //------//
    // getY //
    //------//
    public double getY (int index)
    {
        return ys.get(index);
    }

    //--------//
    // append //
    //--------//
    /**
     * Append a filament to the series
     * @param filament the filament to append
     * @param y the filament ordinate at x abscissa
     */
    public void append (LineFilament filament,
                        double       y)
    {
        filaments.add(filament);
        ys.add(y);
        filament.addPattern(col, this); // Link back Fil -> Pattern
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder("{");
        sb.append("Pattern");

        sb.append(" col:")
          .append(col);

        sb.append(" ")
          .append(filaments.size());

        for (int i = 0; i < filaments.size(); i++) {
            LineFilament fil = filaments.get(i)
                                        .getAncestor();
            double       y = ys.get(i);
            sb.append(" F#")
              .append(fil.getId())
              .append("@")
              .append(y);
        }

        sb.append("}");

        return sb.toString();
    }
}
