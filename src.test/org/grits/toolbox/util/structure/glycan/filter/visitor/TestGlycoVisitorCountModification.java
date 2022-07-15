package org.grits.toolbox.util.structure.glycan.filter.visitor;

import javax.xml.bind.JAXBException;

import org.eurocarbdb.MolecularFramework.io.SugarImporterException;
import org.eurocarbdb.MolecularFramework.io.GlycoCT.SugarImporterGlycoCTCondensed;
import org.eurocarbdb.MolecularFramework.sugar.ModificationType;
import org.eurocarbdb.MolecularFramework.sugar.Sugar;
import org.grits.toolbox.util.structure.glycan.filter.GlycanFilterOperator;
import org.grits.toolbox.util.structure.glycan.filter.XMLUtil;
import org.grits.toolbox.util.structure.glycan.filter.om.GlycanFilterModification;

public class TestGlycoVisitorCountModification
{
    private static Sugar getSugar() throws SugarImporterException
    {
        // Man3 - core fuc + NeuAc + NeuGc + Neu9a
        String t_sequence = "RES\n1b:b-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n8s:n-acetyl\n9b:a-dman-HEX-1:5\n10b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d|9:a\n11s:amino\n12b:a-dgro-dgal-NON-2:6|1:a|2:keto|3:d\n13s:n-glycolyl\n14b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:6o(4+2)7d\n7:7d(5+1)8n\n8:5o(6+1)9d\n9:9o(2+2)10d\n10:10d(5+1)11n\n11:9o(4+2)12d\n12:12d(5+1)13n\n13:1o(6+1)14d";
        SugarImporterGlycoCTCondensed t_importer = new SugarImporterGlycoCTCondensed();
        return t_importer.parse(t_sequence);
    }

    public static void main(String[] args) throws JAXBException
    {
        TestGlycoVisitorCountModification.testXML();
        // test for terminal ms
        TestGlycoVisitorCountModification.testAcid();
    }

    private static void testXML() throws JAXBException
    {
        GlycanFilterModification t_filter = new GlycanFilterModification();
        t_filter.setName("Name");
        t_filter.setLabel("Label");
        t_filter.setDescription("Description");
        t_filter.setMin(0);
        t_filter.setMax(5);
        t_filter.setModification(ModificationType.DOUBLEBOND.getName());
        t_filter.setPositionOne(3);
        t_filter.setPositionTwo(4);
        XMLUtil.testXML(t_filter);
    }

    private static void testAcid()
    {
        try
        {
            // build the sugar
            Sugar t_sugar = TestGlycoVisitorCountModification.getSugar();
            // build the filter
            GlycanFilterModification t_filter = new GlycanFilterModification();
            t_filter.setModification(ModificationType.ACID.getName());
            // run test
            System.out.println("Search for ACID (no position):"
                    + GlycanFilterOperator.applyModificationFilter(t_sugar, t_filter));
            t_filter.setPositionOne(1);
            // run test
            System.out.println("Search for ACID (with position 1): "
                    + GlycanFilterOperator.applyModificationFilter(t_sugar, t_filter));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
