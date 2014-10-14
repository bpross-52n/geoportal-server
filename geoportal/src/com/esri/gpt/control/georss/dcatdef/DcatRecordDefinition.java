/*
 * Copyright 2014 Esri, Inc..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esri.gpt.control.georss.dcatdef;

import com.esri.gpt.control.georss.DcatSchemas;
import com.esri.gpt.control.georss.IFeedRecord;
import static com.esri.gpt.framework.util.Val.chkStr;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Esri, Inc.
 */
public class DcatRecordDefinition {
  private static final ArrayList<DcatFieldDefinition> fieldDefinitions = new ArrayList<DcatFieldDefinition>();
  static {
    fieldDefinitions.add(new StringField("title",DcatFieldDefinition.OBLIGATORY));
    fieldDefinitions.add(new StringField("description",DcatFieldDefinition.OBLIGATORY));
    fieldDefinitions.add(new ArrayField ("keyword",DcatFieldDefinition.OBLIGATORY));
    fieldDefinitions.add(new DateField  ("modified",DcatFieldDefinition.OBLIGATORY));
    fieldDefinitions.add(new PublisherField());
    fieldDefinitions.add(new ContactPointField());
    fieldDefinitions.add(new StringField("identifier",DcatFieldDefinition.OBLIGATORY));
    fieldDefinitions.add(new StringField ("accessLevel",DcatFieldDefinition.OBLIGATORY){
      @Override
      protected String getDefaultValue(Properties properties) {
        return chkStr(properties.getProperty(fldName),"public");
      }
    });
    fieldDefinitions.add(new ArrayField("bureauCode",DcatFieldDefinition.OBLIGATORY){
      @Override
      protected List<String> getDefaultValue(Properties properties) {
        List<String> list = Arrays.asList(chkStr(properties.getProperty(fldName)).replaceAll("^\\p{Space}*\\[|\\]\\p{Space}*$", "").split(","));
        for (int i=0; i<list.size(); i++) {
          list.set(i, list.get(i).replaceAll("^\\p{Space}*\\\"|\\\"\\p{Space}*$", ""));
        }
        return list;
      }
    });
    fieldDefinitions.add(new ArrayField("programCode",DcatFieldDefinition.OBLIGATORY){
      @Override
      protected List<String> getDefaultValue(Properties properties) {
        List<String> list = Arrays.asList(chkStr(properties.getProperty(fldName)).replaceAll("^\\p{Space}*\\[|\\]\\p{Space}*$", "").split(","));
        for (int i=0; i<list.size(); i++) {
          list.set(i, list.get(i).replaceAll("^\\p{Space}*\\\"|\\\"\\p{Space}*$", ""));
        }
        return list;
      }
    });
  }
  public void print(DcatPrinter printer, Properties properties, DcatSchemas dcatSchemas, IFeedRecord r) throws IOException {
    printer.startObject();
    for (DcatFieldDefinition fd: fieldDefinitions) {
      fd.print(printer, properties, dcatSchemas, r);
    }
    printer.endObject();
  }
}
