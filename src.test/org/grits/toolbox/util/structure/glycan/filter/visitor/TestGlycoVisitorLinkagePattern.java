package org.grits.toolbox.util.structure.glycan.filter.visitor;

import javax.xml.bind.JAXBException;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.eurocarbdb.MolecularFramework.util.visitor.GlycoVisitorException;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterException;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterOperator;
import org.grits.toolbox.util.structure.glycan.filter.XMLUtil;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterLinkage;
import org.grits.toolbox.util.structure.glycan.filter.om.MonosaccharideDefintion;

public class TestGlycoVisitorLinkagePattern
{
    private static Sugar getSugar() throws SugarImporterException
    {
        String t_sequence = "RES\n1b:a-dglc-HEX-1:5\n2b:a-dglc-HEX-1:5\n3b:a-dglc-HEX-1:5\n4b:a-dglc-HEX-1:5\n5b:a-dglc-HEX-1:5|6:d\nLIN\n1:1o(3+1)2d\n2:2o(3+1)3d\n3:3o(6+1)4d\n4:2o(4+1)5d";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    public static void main(String[] args) throws GlycoVisitorException, GlycanFilterException,
            SugarImporterException, JAXBException
    {
        TestGlycoVisitorLinkagePattern.testXML();
        // test for terminal ms
        TestGlycoVisitorLinkagePattern.testParsting();
        TestGlycoVisitorLinkagePattern.testParentLinkage();
        TestGlycoVisitorLinkagePattern.testTerminal();
    }

    private static void testTerminal() throws SugarImporterException, GlycanFilterException
    {
        Sugar t_sugar = TestGlycoVisitorLinkagePattern.getSugar();
        GlycanFilterLinkage t_filter = new GlycanFilterLinkage();
        String t_pattern = "6";
        t_filter.setChosenLinkagePattern(t_pattern);
        MonosaccharideDefintion t_defintionMs = new MonosaccharideDefintion();
        t_defintionMs.setAllowModifications(false);
        t_defintionMs.setAllowSubstituents(false);
        t_defintionMs.setSequence("RES\n1b:a-dglc-HEX-1:5");
        t_filter.setMonosaccharide(t_defintionMs);
        t_filter.setTerminal(true);
        // run test
        System.out.println(t_pattern + ": "
                + GlycanFilterOperator.applyLinkagePatternFilter(t_sugar, t_filter));
        // other part
        t_pattern = " 3";
        t_filter.setChosenLinkagePattern(t_pattern);
        // run test
        System.out.println(t_pattern + ": "
                + GlycanFilterOperator.applyLinkagePatternFilter(t_sugar, t_filter));
    }

    private static void testXML() throws JAXBException
    {
        GlycanFilterLinkage t_filter = new GlycanFilterLinkage();
        t_filter.setName("Name");
        t_filter.setLabel("Label");
        t_filter.setDescription("Description");
        // chosen pattern
        String t_pattern = "3,6";
        t_filter.setChosenLinkagePattern(t_pattern);
        // monosaccharide
        MonosaccharideDefintion t_defintionMs = new MonosaccharideDefintion();
        t_defintionMs.setAllowModifications(false);
        t_defintionMs.setAllowSubstituents(false);
        t_defintionMs.setSequence("RES\n1b:a-dglc-HEX-1:5");
        t_filter.setMonosaccharide(t_defintionMs);
        // terminal
        t_filter.setTerminal(true);
        XMLUtil.testXML(t_filter);
    }

    private static void testParentLinkage() throws GlycanFilterException, SugarImporterException
    {
        Sugar t_sugar = TestGlycoVisitorLinkagePattern.getSugar();
        GlycanFilterLinkage t_filter = new GlycanFilterLinkage();
        String t_pattern = " 3,6";
        t_filter.setChosenLinkagePattern(t_pattern);
        MonosaccharideDefintion t_defintionMs = new MonosaccharideDefintion();
        t_defintionMs.setAllowModifications(false);
        t_defintionMs.setAllowSubstituents(false);
        t_defintionMs.setSequence("RES\n1b:a-dglc-HEX-1:5");
        t_filter.setMonosaccharide(t_defintionMs);
        // run test
        System.out.println(t_pattern + ": "
                + GlycanFilterOperator.applyLinkagePatternFilter(t_sugar, t_filter));

    }

    private static void testParsting() throws GlycoVisitorException
    {
        GlycoVisitorLinakgePattern t_visitorLinakgePattern = new GlycoVisitorLinakgePattern();

        t_visitorLinakgePattern.linkagePattern(" 2,3 ");
    }

}
