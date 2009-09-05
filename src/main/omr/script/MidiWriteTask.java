//----------------------------------------------------------------------------//
//                                                                            //
//                         M i d i W r i t e T a s k                          //
//                                                                            //
//  Copyright (C) Herve Bitteur 2000-2009. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Please contact users@audiveris.dev.java.net to report bugs & suggestions. //
//----------------------------------------------------------------------------//
package omr.script;

import omr.log.Logger;

import omr.score.ScoreManager;

import omr.sheet.Sheet;

import omr.step.StepException;

import java.io.File;

import javax.xml.bind.annotation.*;

/**
 * Class <code>MidiWriteTask</code> is a script task which writes the Midi file
 * of a score
 *
 * @author Herv&eacute Bitteur
 * @version $Id$
 */
@XmlAccessorType(XmlAccessType.NONE)
public class MidiWriteTask
    extends ScriptTask
{
    //~ Instance fields --------------------------------------------------------

    /** The file used for export */
    @XmlAttribute
    private String path;

    //~ Constructors -----------------------------------------------------------

    //---------------//
    // MidiWriteTask //
    //---------------//
    /**
     * Create a task to write the midi file of a sheet
     *
     * @param path the full path of the midi file
     */
    public MidiWriteTask (String path)
    {
        this.path = path;
    }

    //---------------//
    // MidiWriteTask //
    //---------------//
    /** No-arg constructor needed by JAXB */
    private MidiWriteTask ()
    {
    }

    //~ Methods ----------------------------------------------------------------

    //-----//
    // run //
    //-----//
    @Override
    public void run (Sheet sheet)
        throws Exception
    {
        ScoreManager.getInstance()
                    .midiWrite(sheet.getScore(), new File(path));
    }

    //-----------------//
    // internalsString //
    //-----------------//
    @Override
    protected String internalsString ()
    {
        return " write " + path;
    }
}
