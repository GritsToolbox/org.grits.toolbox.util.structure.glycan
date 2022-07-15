package org.grits.toolbox.util.structure.glycan.filter;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.grits.toolbox.core.dataShare.PropertyHandler;
import org.grits.toolbox.util.structure.glycan.filter.om.Filter;
import org.grits.toolbox.util.structure.glycan.filter.om.FiltersLibrary;
import org.grits.toolbox.util.structure.glycan.util.FilterUtils;

public class XMLUtil
{
    @SuppressWarnings("rawtypes")
    public static String toXML(FiltersLibrary a_lib) throws JAXBException
    {
        StringWriter t_writer = new StringWriter();
        List<Class> t_contextList = new ArrayList<Class>(
                Arrays.asList(FilterUtils.filterClassContext));
        t_contextList.add(FiltersLibrary.class);
        JAXBContext t_context = JAXBContext
                .newInstance(t_contextList.toArray(new Class[t_contextList.size()]));
        Marshaller t_marshaller = t_context.createMarshaller();
        t_marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        t_marshaller.setProperty(Marshaller.JAXB_ENCODING,
                PropertyHandler.GRITS_CHARACTER_ENCODING);
        t_marshaller.marshal(a_lib, t_writer);
        return t_writer.toString();
    }

    @SuppressWarnings("rawtypes")
    public static FiltersLibrary fromXML(String a_xml) throws JAXBException
    {
        StringReader t_reader = new StringReader(a_xml);
        List<Class> t_contextList = new ArrayList<>(Arrays.asList(FilterUtils.filterClassContext));
        t_contextList.add(FiltersLibrary.class);
        JAXBContext t_context = JAXBContext
                .newInstance(t_contextList.toArray(new Class[t_contextList.size()]));
        Unmarshaller t_unmarshaller = t_context.createUnmarshaller();
        FiltersLibrary t_filtersLibrary = (FiltersLibrary) t_unmarshaller.unmarshal(t_reader);
        return t_filtersLibrary;
    }

    public static void testXML(Filter a_filter) throws JAXBException
    {
        System.out.println("Generating XML:");
        FiltersLibrary t_lib = new FiltersLibrary();
        t_lib.addFilter(a_filter);
        String t_xml = XMLUtil.toXML(t_lib);
        System.out.println(t_xml);
        System.out.println("Parsing XML:");
        t_lib = XMLUtil.fromXML(t_xml);
        System.out.println("Success");
    }
}
