//-----------------------------------------------------------------------//
//                                                                       //
//                       S c r o l l L a g V i e w                       //
//                                                                       //
//  Copyright (C) Herve Bitteur 2000-2005. All rights reserved.          //
//  This software is released under the terms of the GNU General Public  //
//  License. Please contact the author at herve.bitteur@laposte.net      //
//  to report bugs & suggestions.                                        //
//-----------------------------------------------------------------------//

package omr.ui;

import omr.lag.LagView;
import omr.lag.Section;
import omr.util.Logger;

/**
 * Class <code>ScrollLagView</code> is a customized {@link ScrollView}
 * dedicated to the display of a {@link omr.lag.LagView}, with monitoring
 * of run and section informations
 *
 * @author Herv&eacute Bitteur
 * @version $Id$
 */
public class ScrollLagView
    extends ScrollView
{
    //~ Static variables/initializers -------------------------------------

    private static Logger logger = Logger.getLogger(ScrollLagView.class);

    //~ Constructors ------------------------------------------------------

    //---------------//
    // ScrollLagView //
    //---------------//

    /**
     * Create a scroll view on a lag.
     *
     * @param view the contained {@link omr.lag.LagView}
     */
    public ScrollLagView (LagView view)
    {
        setView(view);
    }

    //~ Methods -----------------------------------------------------------

    //---------//
    // getView //
    //---------//
    public LagView getView ()
    {
        return (LagView) view;
    }
}
