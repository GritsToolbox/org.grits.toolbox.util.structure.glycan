package org.grits.toolbox.util.structure.glycan.filter.visitor;

import javax.xml.bind.JAXBException;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterOperator;
import org.grits.toolbox.util.structure.glycan.filter.XMLUtil;
import org.grits.toolbox.util.structure.glycan.filter.om.FilterSequence;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterSubstructure;

public class TestGlycoVisitorSubstucture
{
    private static void testSubstructureFilter()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorSubstucture.getSugar();
            // build the filter
            GlycanFilterSubstructure t_filter = new GlycanFilterSubstructure();
            FilterSequence t_sequence = new FilterSequence();
            t_sequence.setSequence("RES\n1b:x-dgal-HEX-1:5\n2b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d");
            t_sequence.setReducingEnd(false);
            t_filter.getSubstructure().add(t_sequence);
            // run test
            System.out.println(
                    "GalGal: " + GlycanFilterOperator.applySubstructureFilter(t_sugar, t_filter));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Sugar getSugar() throws SugarImporterException
    {
        String t_sequence = "RES\n1b:b-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:b-dglc-HEX-1:5\n8s:n-acetyl\n9b:b-dgal-HEX-1:5\n10s:phosphate\n11b:a-lgal-HEX-1:5|6:d\n12b:a-dman-HEX-1:5\n13b:b-dglc-HEX-1:5\n14s:n-acetyl\n15b:b-dgal-HEX-1:5\n16b:b-dgal-HEX-1:5\n17b:b-dglc-HEX-1:5\n18s:n-acetyl\n19b:b-dgal-HEX-1:5\n20b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n21s:n-acetyl\n22b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:6o(2+1)7d\n7:7d(2+1)8n\n8:7o(4+1)9d\n9:9o(6+1)10n\n10:7o(6+1)11d\n11:5o(6+1)12d\n12:12o(2+1)13d\n13:13d(2+1)14n\n14:13o(4+1)15d\n15:15o(4+1)16d\n16:12o(4+1)17d\n17:17d(2+1)18n\n18:17o(4+1)19d\n19:19o(4+2)20d\n20:20d(5+1)21n\n21:1o(6+1)22d\n";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    public static void main(String[] args) throws JAXBException
    {
        TestGlycoVisitorSubstucture.testXML();
        // test for terminal ms
        TestGlycoVisitorSubstucture.testSubstructureFilter();
    }

    private static void testXML() throws JAXBException
    {
        GlycanFilterSubstructure t_filter = new GlycanFilterSubstructure();
        t_filter.setName("Name");
        t_filter.setLabel("Label");
        t_filter.setDescription("Description");
        FilterSequence t_sequence = new FilterSequence();
        t_sequence.setSequence("RES\n1b:x-dgal-HEX-1:5\n2b:b-dgal-HEX-1:5\nLIN\n1:1o(4+1)2d");
        t_sequence.setReducingEnd(false);
        t_filter.getSubstructure().add(t_sequence);
        t_sequence = new FilterSequence();
        t_sequence.setSequence("RES\n1b:x-dgal-HEX-1:5\n2b:b-dgal-HEX-1:5\nLIN\n1:1o(3+1)2d");
        t_sequence.setReducingEnd(true);
        t_filter.getSubstructure().add(t_sequence);

        XMLUtil.testXML(t_filter);
    }

}
