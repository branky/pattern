/*
 * Copyright (c) 2007-2013 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.concurrentinc.com/
 */

package pattern;

import javax.xml.xpath.XPathConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class PMML
  {
  /** Field LOG */
  private static final Logger LOG = LoggerFactory.getLogger( PMML.class );

  protected XPathReader reader;
  protected Schema schema = new Schema();

  /** Implemented model types */
  public enum Models { UNKNOWN, MINING, TREE, REGRESSION };

  public Models model_type = Models.UNKNOWN;
  public String version;

  /**
   * Parse the XML in the PMML description.
   *
   * @param pmml_file PMML file
   * @throws PatternException
   */
  public PMML( String pmml_file ) throws PatternException
    {
    reader = new XPathReader( pmml_file );

    version = ( (Element) getNodeList( "/PMML" ).item( 0 ) ).getAttribute( "version" );
    model_type = parseModelType();

    LOG.info( String.format( "Model: %s", model_type.name() ) );
    LOG.info( String.format( "Version: %s", version ) );

    schema.parseDictionary( this, getNodeList( "/PMML/DataDictionary/DataField" ) );
    }

  /**
   * Parse the model type.
   *
   * @return Models
   */
  public Models parseModelType()
    {
    Models model_type = Models.UNKNOWN;

    if( reader.read( "/PMML/MiningModel", XPathConstants.NODE ) != null )
      return Models.MINING;
    else if( reader.read( "/PMML/TreeModel", XPathConstants.NODE ) != null )
      return Models.TREE;
    else if( reader.read( "/PMML/RegressionModel", XPathConstants.NODE ) != null )
      return Models.REGRESSION;

    return model_type;
    }

  /**
   * Getter for the PMML data dictionary.
   *
   * @return Schema
   */
  public Schema getSchema()
    {
    return schema;
    }

  /**
   * Getter for the XML document reader.
   *
   * @return XPathReader
   */
  public XPathReader getReader()
    {
    return reader;
    }

  /**
   * Extract an XML node list based on an XPath expression.
   *
   * @param expr XPath expression to evaluate
   * @return NodeList
   */
  public NodeList getNodeList( String expr )
    {
    return (NodeList) reader.read( expr, XPathConstants.NODESET );
    }
  }
