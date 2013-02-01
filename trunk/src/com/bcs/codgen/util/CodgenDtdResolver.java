package com.bcs.codgen.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * 代码生成器DTD配置文件检查器
 * @author 黄天政
 *
 */
public class CodgenDtdResolver implements EntityResolver {
	private static final String DTD_NAME = "codgen-config";
	private static final String SEARCH_PACKAGE = "com/bcs/codgen/resources/";
	protected final Log logger = LogFactory.getLog(getClass());

	public InputSource resolveEntity(String publicId, String systemId)
			throws IOException {
		logger.debug("Trying to resolve XML entity with public ID [" + publicId
				+ "] and system ID [" + systemId + "]");
		if (systemId != null
				&& systemId.indexOf(DTD_NAME) > systemId.lastIndexOf("/")) {
			String dtdFile = systemId.substring(systemId.indexOf(DTD_NAME));
			logger.debug("Trying to locate [" + dtdFile + "] under ["
					+ SEARCH_PACKAGE + "]");
			InputStream inputStream = ClassLoaderUtil.getStream(SEARCH_PACKAGE+dtdFile);
			InputSource source = new InputSource(inputStream);
			source.setPublicId(publicId);
			source.setSystemId(systemId);
			logger.debug("Found beans DTD [" + systemId + "] in classpath");
			return source;
		}
		// use the default behaviour -> download from website or wherever
		return null;
	}

}
