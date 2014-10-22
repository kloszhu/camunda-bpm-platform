/* Licensed under the Apache License, Version 2.0 (the "License");
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
package org.camunda.bpm.engine.rest.impl.history;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.UriInfo;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.history.HistoricCaseInstance;
import org.camunda.bpm.engine.history.HistoricCaseInstanceQuery;
import org.camunda.bpm.engine.rest.dto.CountResultDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseInstanceDto;
import org.camunda.bpm.engine.rest.dto.history.HistoricCaseInstanceQueryDto;
import org.camunda.bpm.engine.rest.history.HistoricCaseInstanceRestService;
import org.camunda.bpm.engine.rest.sub.history.HistoricCaseInstanceResource;
import org.camunda.bpm.engine.rest.sub.history.impl.HistoricCaseInstanceResourceImpl;

public class HistoricCaseInstanceRestServiceImpl implements HistoricCaseInstanceRestService {

  protected ProcessEngine processEngine;

  public HistoricCaseInstanceRestServiceImpl(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }

  public HistoricCaseInstanceResource getHistoricCaseInstance(String processInstanceId) {
    return new HistoricCaseInstanceResourceImpl(processEngine, processInstanceId);
  }

  public List<HistoricCaseInstanceDto> getHistoricCaseInstances(UriInfo uriInfo, Integer firstResult, Integer maxResults) {
    HistoricCaseInstanceQueryDto queryHistoricCaseInstanceDto = new HistoricCaseInstanceQueryDto(uriInfo.getQueryParameters());
    return queryHistoricCaseInstances(queryHistoricCaseInstanceDto, firstResult, maxResults);
  }

  public List<HistoricCaseInstanceDto> queryHistoricCaseInstances(HistoricCaseInstanceQueryDto queryDto, Integer firstResult, Integer maxResults) {
    HistoricCaseInstanceQuery query = queryDto.toQuery(processEngine);

    List<HistoricCaseInstance> matchingHistoricCaseInstances;
    if (firstResult != null || maxResults != null) {
      matchingHistoricCaseInstances = executePaginatedQuery(query, firstResult, maxResults);
    } else {
      matchingHistoricCaseInstances = query.list();
    }

    List<HistoricCaseInstanceDto> historicCaseInstanceDtoResults = new ArrayList<HistoricCaseInstanceDto>();
    for (HistoricCaseInstance historicCaseInstance : matchingHistoricCaseInstances) {
      HistoricCaseInstanceDto resultHistoricCaseInstanceDto = HistoricCaseInstanceDto.fromHistoricCaseInstance(historicCaseInstance);
      historicCaseInstanceDtoResults.add(resultHistoricCaseInstanceDto);
    }
    return historicCaseInstanceDtoResults;
  }

  private List<HistoricCaseInstance> executePaginatedQuery(HistoricCaseInstanceQuery query, Integer firstResult, Integer maxResults) {
    if (firstResult == null) {
      firstResult = 0;
    }
    if (maxResults == null) {
      maxResults = Integer.MAX_VALUE;
    }
    return query.listPage(firstResult, maxResults);
  }

  public CountResultDto getHistoricCaseInstancesCount(UriInfo uriInfo) {
    HistoricCaseInstanceQueryDto queryDto = new HistoricCaseInstanceQueryDto(uriInfo.getQueryParameters());
    return queryHistoricCaseInstancesCount(queryDto);
  }

  public CountResultDto queryHistoricCaseInstancesCount(HistoricCaseInstanceQueryDto queryDto) {
    HistoricCaseInstanceQuery query = queryDto.toQuery(processEngine);

    long count = query.count();

    return new CountResultDto(count);
  }

}
