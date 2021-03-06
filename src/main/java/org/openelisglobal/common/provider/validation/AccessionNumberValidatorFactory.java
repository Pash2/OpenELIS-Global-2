/**
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
* Copyright (C) The Minnesota Department of Health.  All Rights Reserved.
*
* Contributor(s): CIRG, University of Washington, Seattle WA.
*/
package org.openelisglobal.common.provider.validation;

import org.openelisglobal.common.exception.LIMSInvalidConfigurationException;
import org.openelisglobal.common.util.ConfigurationProperties;
import org.openelisglobal.common.util.ConfigurationProperties.Property;

public class AccessionNumberValidatorFactory {

    public enum AccessionFormat {
        DEFAULT, SITE_YEAR, PROGRAM, YEAR_NUM_SIX, YEAR_NUM_DASH, YEAR_NUM_SEVEN, ANY
    }

    private IAccessionNumberValidator validator;

    public IAccessionNumberValidator getValidator() throws LIMSInvalidConfigurationException {

        String accessionFormat = ConfigurationProperties.getInstance()
                .getPropertyValueUpperCase(Property.AccessionFormat);

        if (accessionFormat.equals("SITEYEARNUM")) {
            return getSiteYearValidator();
        } else if (accessionFormat.equals("PROGRAMNUM")) {
            return getProgramValidator();
        }
        if (accessionFormat.equals("YEARNUM_SIX")) {
            return getYearNumValidator(6, null);
        }
        if (accessionFormat.equals("YEARNUM_DASH_SEVEN")) {
            return getYearNumValidator(7, '-');
        }
        if (accessionFormat.equals("YEARNUM_SEVEN")) {
            return getYearNumValidator(7, null);
        }

        throw new LIMSInvalidConfigurationException(
                "AccessionNumberValidatorFactory: Unable to find validator for " + accessionFormat);
    }

    public IAccessionNumberValidator getValidator(String accessionFormat) throws LIMSInvalidConfigurationException {

        if (accessionFormat.equals("SITEYEARNUM")) {
            return getSiteYearValidator();
        } else if (accessionFormat.equals("PROGRAMNUM")) {
            return getProgramValidator();
        }
        if (accessionFormat.equals("YEARNUM_SIX")) {
            return getYearNumValidator(6, null);
        }
        if (accessionFormat.equals("YEARNUM_DASH_SEVEN")) {
            return getYearNumValidator(7, '-');
        }
        if (accessionFormat.equals("YEARNUM_SEVEN")) {
            return getYearNumValidator(7, null);
        }

        throw new LIMSInvalidConfigurationException(
                "AccessionNumberValidatorFactory: Unable to find validator for " + accessionFormat);
    }

    // TODO move all getValidator(s) to use the enum approach
    public IAccessionNumberValidator getValidator(AccessionFormat accessionFormat)
            throws LIMSInvalidConfigurationException {

        switch (accessionFormat) {
        case DEFAULT:
            return getValidator();
        case SITE_YEAR:
            return getSiteYearValidator();
        case PROGRAM:
            return getProgramValidator();
        case YEAR_NUM_SIX:
            return getYearNumValidator(6, null);
        case YEAR_NUM_DASH:
            return getYearNumValidator(7, '-');
        case YEAR_NUM_SEVEN:
            return getYearNumValidator(7, null);
        default:
            throw new LIMSInvalidConfigurationException(
                    "AccessionNumberValidatorFactory: Unable to find validator for " + accessionFormat);
        }

    }

    @SuppressWarnings("unused")
    private IAccessionNumberValidator getDigitAccessionValidator(int length) {
        synchronized (AccessionNumberValidatorFactory.class) {
            if (validator == null) {
                validator = new DigitAccessionValidator(length);
            }
        }

        return validator;
    }

    private IAccessionNumberValidator getYearNumValidator(int length, Character separator) {
        if (validator == null) {
            validator = new YearNumAccessionValidator(length, separator);
        }

        return validator;
    }

    private IAccessionNumberValidator getSiteYearValidator() {
        if (validator == null) {
            validator = new SiteYearAccessionValidator();
        }

        return validator;
    }

    private IAccessionNumberValidator getProgramValidator() {
        if (validator == null) {
            validator = new ProgramAccessionValidator();
        }

        return validator;
    }

}
