package tk.pratanumandal.fts.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import tk.pratanumandal.fts.bean.FtsConfig;

public class FtsConfigLoader {
	
	public static FtsConfig load(File file) throws JAXBException {
		
		JAXBContext jaxbContext = JAXBContext.newInstance(FtsConfig.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (FtsConfig) jaxbUnmarshaller.unmarshal(file);
	}
	
}
