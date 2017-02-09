/*
 * Copyright 2017 S. Koulouzis.
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
package nl.uva.sne.drip.commons.types;

import java.util.Date;
import java.util.List;

/**
 *
 * @author S. Koulouzis
 */
public interface IMessage {

    public static final String CREATION_DATE = "creationDate";

    public Long getCreationDate();

    public void setCreationDate(Long creationDate);

    public void setParameters(List<Parameter> params);

    public List getParameters();

}
