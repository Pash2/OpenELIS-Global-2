/*
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is OpenELIS code.
 *
 * Copyright (C) ITECH, University of Washington, Seattle WA.  All Rights Reserved.
 */

package us.mn.state.health.lims.testconfiguration.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.service.panel.PanelService;
import spring.service.typeofsample.TypeOfSamplePanelService;
import spring.service.typeofsample.TypeOfSampleService;
import us.mn.state.health.lims.panel.dao.PanelDAO;
import us.mn.state.health.lims.panel.daoimpl.PanelDAOImpl;
import us.mn.state.health.lims.panel.valueholder.Panel;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSampleDAO;
import us.mn.state.health.lims.typeofsample.dao.TypeOfSamplePanelDAO;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSampleDAOImpl;
import us.mn.state.health.lims.typeofsample.daoimpl.TypeOfSamplePanelDAOImpl;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSample;
import us.mn.state.health.lims.typeofsample.valueholder.TypeOfSamplePanel;

@Service
public class PanelTestConfigurationUtil {

	@Autowired
	TypeOfSamplePanelService typeOfSamplePanelService ;
	@Autowired
	PanelService panelService;
	@Autowired
	TypeOfSampleService typeOfSampleService;
	
	@SuppressWarnings("unchecked")
	 public HashMap<String, List<Panel>> createTypeOfSamplePanelMap(boolean isActive) {
		HashMap<String, List<Panel>> sampleTypeMap = new HashMap<String, List<Panel>>();
		
		
		List<TypeOfSamplePanel> listOfTypeOfSamplePanels = typeOfSamplePanelService.getAllTypeOfSamplePanels();
		for (TypeOfSamplePanel typeOfSamplePanel : listOfTypeOfSamplePanels) {
			TypeOfSample typeOfSample = typeOfSampleService.getTypeOfSampleById(typeOfSamplePanel.getTypeOfSampleId());
			List<Panel> panelsForThisSampleType = sampleTypeMap.get(typeOfSample.getLocalizedName());
			if (panelsForThisSampleType == null) {
				panelsForThisSampleType = new ArrayList<Panel>();
				sampleTypeMap.put(typeOfSample.getLocalizedName(), panelsForThisSampleType);
			}
			Panel panel = panelService.getPanelById(typeOfSamplePanel.getPanelId());

			if ("Y".equals(panel.getIsActive()) && isActive) {
				sampleTypeMap.get(typeOfSample.getLocalizedName()).add(panel);
			} else if (!"Y".equals(panel.getIsActive()) && !isActive) {
				sampleTypeMap.get(typeOfSample.getLocalizedName()).add(panel);
			}
		}
		
		return sampleTypeMap;
	}	

	@SuppressWarnings("unchecked")
    public HashMap<String, List<Panel>> createTypeOfSamplePanelMap() {
		HashMap<String, List<Panel>> sampleTypeMap = new HashMap<String, List<Panel>>();
		
		List<TypeOfSamplePanel> listOfTypeOfSamplePanels = typeOfSamplePanelService.getAllTypeOfSamplePanels();
		for (TypeOfSamplePanel typeOfSamplePanel : listOfTypeOfSamplePanels) {
			TypeOfSample typeOfSample = typeOfSampleService.getTypeOfSampleById(typeOfSamplePanel.getTypeOfSampleId());
			List<Panel> panelsForThisSampleType = sampleTypeMap.get(typeOfSample.getLocalizedName());
			if (panelsForThisSampleType == null) {
				panelsForThisSampleType = new ArrayList<Panel>();
				sampleTypeMap.put(typeOfSample.getLocalizedName(), panelsForThisSampleType);
			}
			Panel panel = panelService.getPanelById(typeOfSamplePanel.getPanelId());

			sampleTypeMap.get(typeOfSample.getLocalizedName()).add(panel);
		}
		
		return sampleTypeMap;
	}	

}
