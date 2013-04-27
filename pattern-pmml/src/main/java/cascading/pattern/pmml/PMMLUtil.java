/*
 * Copyright (c) 2007-2013 Concurrent, Inc. All Rights Reserved.
 *
 * Project and contact information: http://www.cascading.org/
 *
 * This file is part of the Cascading project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cascading.pattern.pmml;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.dmg.pmml.ArrayType;
import org.dmg.pmml.Value;
import org.jpmml.evaluator.ArrayUtil;

/**
 *
 */
public class PMMLUtil
  {
  public static List parseArray( ArrayType arrayType )
    {
    List<String> tokenize = ArrayUtil.tokenize( arrayType );

    List result;

    if( arrayType.getType() == ArrayType.Type.REAL )
      result = Lists.transform( tokenize, new Function<String, Double>()
      {
      @Override
      public Double apply( String input )
        {
        return Double.parseDouble( input );
        }
      } );
    else if( arrayType.getType() == ArrayType.Type.INT )
      result = Lists.transform( tokenize, new Function<String, Integer>()
      {
      @Override
      public Integer apply( String input )
        {
        return Integer.parseInt( input );
        }
      } );
    else if( arrayType.getType() == ArrayType.Type.STRING )
      result = tokenize;
    else
      throw new UnsupportedOperationException( "unknown array type: " + arrayType.getType() );

    return new ArrayList( result ); // minimize serialization closure
    }

  public static List<String> asStrings( List<Value> results )
    {
    List<String> result = Lists.transform( results, new Function<Value, String>()
    {
    @Override
    public String apply( Value input )
      {
      if( input.getProperty() != Value.Property.VALID )
        throw new UnsupportedOperationException( "data field property not supported: " + input.getProperty() );

      return input.getValue();
      }
    } );

    return new ArrayList<String>( result ); // minimize serialization closure
    }
  }
