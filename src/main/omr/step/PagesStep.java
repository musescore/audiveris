//----------------------------------------------------------------------------//
//                                                                            //
//                             P a g e s S t e p                              //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright (C) Herve Bitteur 2000-2010. All rights reserved.               //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.step;

import omr.Main;

import omr.constant.Constant;
import omr.constant.ConstantSet;

import omr.log.Logger;

import omr.score.BeamReader;
import omr.score.ScoreChecker;
import omr.score.ScoreCleaner;
import omr.score.TimeSignatureFixer;
import omr.score.TimeSignatureRetriever;
import omr.score.entity.Page;
import omr.score.entity.ScoreSystem;
import omr.score.midi.MidiAgent;

import omr.sheet.Sheet;
import omr.sheet.SystemInfo;

import omr.util.WrappedBoolean;

import java.util.Collection;

/**
 * Class {@code PagesStep} translates glyphs into score entities for a page.
 *
 * @author Hervé Bitteur
 */
public class PagesStep
    extends AbstractSystemStep
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(PagesStep.class);

    //~ Constructors -----------------------------------------------------------

    //-----------//
    // PagesStep //
    //-----------//
    /**
     * Creates a new PagesStep object.
     */
    public PagesStep ()
    {
        super(
            Steps.PAGES,
            Level.SHEET_LEVEL,
            Mandatory.MANDATORY,
            Redoable.REDOABLE,
            SYMBOLS_TAB,
            "Translate glyphs to score items");
    }

    //~ Methods ----------------------------------------------------------------

    //-----------//
    // displayUI //
    //-----------//
    @Override
    public void displayUI (Sheet sheet)
    {
        Steps.valueOf(Steps.SYMBOLS)
             .displayUI(sheet);
    }

    //----------//
    // doSystem //
    //----------//
    @Override
    public void doSystem (SystemInfo system)
        throws StepException
    {
        final int            iterMax = constants.maxPageIterations.getValue();
        final ScoreSystem    scoreSystem = system.getScoreSystem();
        final WrappedBoolean modified = new WrappedBoolean(true);

        // Purge system of non-active glyphs
        system.removeInactiveGlyphs();

        for (int iter = 1; modified.isSet() && (iter <= iterMax); iter++) {
            modified.set(false);

            if (logger.isFineEnabled()) {
                logger.fine(
                    "System#" + system.getId() + " translation iter #" + iter);
            }

            // Clear errors for this system only (and this step)
            if (Main.getGui() != null) {
                system.getSheet()
                      .getErrorsEditor()
                      .clearSystem(this, system.getId());
            }

            // Cleanup the system, staves, measures, barlines, ...
            // and clear glyph (& sentence) translations
            scoreSystem.accept(new ScoreCleaner());

            // Real translation
            system.translateSystem();

            /** Final checks at system level */
            scoreSystem.acceptChildren(new ScoreChecker(modified));
        }
    }

    //----------//
    // doEpilog //
    //----------//
    @Override
    protected void doEpilog (Collection<SystemInfo> systems,
                             Sheet                  sheet)
        throws StepException
    {
        // Final cross-system translation tasks
        if (systems == null) {
            systems = sheet.getSystems();
        }

        if (!systems.isEmpty()) {
            systems.iterator()
                   .next()
                   .translateFinal();

            // Finally, all actions for completed page (in proper order)
            Page page = sheet.getPage();

            // Compute mean beam thickness at page level
            page.accept(new BeamReader());

            // 1/ Look carefully for time signatures
            page.accept(new TimeSignatureRetriever());

            // 2/ Adapt time sigs to intrinsic measure & chord durations
            page.accept(new TimeSignatureFixer());

            if (Main.getGui() != null) {
                try {
                    // Invalidate score data within MidiAgent, if so needed
                    MidiAgent agent = MidiAgent.getInstance();

                    if (agent.getScore() == sheet.getScore()) {
                        agent.reset();
                    }
                } catch (Exception ex) {
                    logger.warning("Cannot access Midi agent", ex);
                }
            }
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
        extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        private final Constant.Integer maxPageIterations = new Constant.Integer(
            "count",
            2,
            "Maximum number of iterations for SCORE task");
    }
}