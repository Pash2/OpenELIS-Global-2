package org.openelisglobal.barcode.controller;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.openelisglobal.barcode.form.BarcodeConfigurationForm;
import org.openelisglobal.barcode.service.BarcodeInformationService;
import org.openelisglobal.common.controller.BaseController;
import org.openelisglobal.common.exception.LIMSRuntimeException;
import org.openelisglobal.common.util.ConfigurationProperties;
import org.openelisglobal.common.util.ConfigurationProperties.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BarcodeConfigurationController extends BaseController {

    private static final String[] ALLOWED_FIELDS = new String[] { "heightOrderLabels", "heightSpecimenLabels",
            "widthOrderLabels", "widthSpecimenLabels", "collectionDateCheck", "testsCheck", "patientSexCheck",
            "numOrderLabels", "numSpecimenLabels" };

    @Autowired
    private BarcodeInformationService barcodeInformationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAllowedFields(ALLOWED_FIELDS);
    }

    @RequestMapping(value = "/BarcodeConfiguration", method = RequestMethod.GET)
    public ModelAndView showBarcodeConfiguration(HttpServletRequest request)
            throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String forward = FWD_SUCCESS;
        BarcodeConfigurationForm form = new BarcodeConfigurationForm();

        addFlashMsgsToRequest(request);
        form.setCancelAction("MasterListsPage.do");

        setFields(form);

        request.getSession().setAttribute(SAVE_DISABLED, "false");

        return findForward(forward, form);
    }

    /**
     * Set the form fields with those values stored in the database
     *
     * @param form The form to populate
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private void setFields(BarcodeConfigurationForm form) {

        // get the dimension values
        String heightOrderLabels = ConfigurationProperties.getInstance()
                .getPropertyValue(Property.ORDER_BARCODE_HEIGHT);
        String widthOrderLabels = ConfigurationProperties.getInstance().getPropertyValue(Property.ORDER_BARCODE_WIDTH);
        String heightSpecimenLabels = ConfigurationProperties.getInstance()
                .getPropertyValue(Property.SPECIMEN_BARCODE_HEIGHT);
        String widthSpecimenLabels = ConfigurationProperties.getInstance()
                .getPropertyValue(Property.SPECIMEN_BARCODE_WIDTH);
        // set the dimension values
        form.setHeightOrderLabels(Float.parseFloat(heightOrderLabels));
        form.setWidthOrderLabels(Float.parseFloat(widthOrderLabels));
        form.setHeightSpecimenLabels(Float.parseFloat(heightSpecimenLabels));
        form.setWidthSpecimenLabels(Float.parseFloat(widthSpecimenLabels));

        // get the maximum print values
        String numOrderLabels = ConfigurationProperties.getInstance().getPropertyValue(Property.MAX_ORDER_PRINTED);
        String numSpecimenLabels = ConfigurationProperties.getInstance()
                .getPropertyValue(Property.MAX_SPECIMEN_PRINTED);
        String numAliquotLabels = ConfigurationProperties.getInstance().getPropertyValue(Property.MAX_ALIQUOT_PRINTED);
        // set the maximum print values
        form.setNumOrderLabels(Integer.parseInt(numOrderLabels));
        form.setNumSpecimenLabels(Integer.parseInt(numSpecimenLabels));
        form.setNumAliquotLabels(Integer.parseInt(numAliquotLabels));

        // get the optional specimen values
        String collectionDateCheck = ConfigurationProperties.getInstance()
                .getPropertyValue(Property.SPECIMEN_FIELD_DATE);
        String testsCheck = ConfigurationProperties.getInstance().getPropertyValue(Property.SPECIMEN_FIELD_TESTS);
        String patientSexCheck = ConfigurationProperties.getInstance().getPropertyValue(Property.SPECIMEN_FIELD_SEX);
        // set the optional specimen values
        form.setCollectionDateCheck(Boolean.valueOf(collectionDateCheck));
        form.setTestsCheck(Boolean.valueOf(testsCheck));
        form.setPatientSexCheck(Boolean.valueOf(patientSexCheck));
    }

    @RequestMapping(value = "/BarcodeConfiguration", method = RequestMethod.POST)
    public ModelAndView barcodeConfigurationSave(HttpServletRequest request,
            @ModelAttribute("form") @Valid BarcodeConfigurationForm form, BindingResult result,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            saveErrors(result);
            form.setCancelAction("MasterListsPage.do");
            return findForward(FWD_FAIL_INSERT, form);
        }

        // ensure transaction block
        try {
            barcodeInformationService.updateBarcodeInfoFromForm(form, getSysUserId(request));
        } catch (LIMSRuntimeException e) {
            result.reject("barcode.config.error.insert");
        } finally {
            ConfigurationProperties.forceReload();
        }

        if (result.hasErrors()) {
            saveErrors(result);
            return findForward(FWD_FAIL_INSERT, form);
        }

        redirectAttributes.addFlashAttribute(FWD_SUCCESS, true);
        return findForward(FWD_SUCCESS_INSERT, form);
    }

    @Override
    protected String findLocalForward(String forward) {
        if (FWD_SUCCESS.equals(forward)) {
            return "BarcodeConfigurationDefinition";
        } else if (FWD_SUCCESS_INSERT.equals(forward)) {
            return "redirect:/BarcodeConfiguration.do";
        } else if (FWD_FAIL_INSERT.equals(forward)) {
            return "BarcodeConfigurationDefinition";
        } else {
            return "PageNotFound";
        }
    }

    @Override
    protected String getPageTitleKey() {
        return "barcodeconfiguration.browse.title";
    }

    @Override
    protected String getPageSubtitleKey() {
        return "barcodeconfiguration.browse.title";
    }
}
