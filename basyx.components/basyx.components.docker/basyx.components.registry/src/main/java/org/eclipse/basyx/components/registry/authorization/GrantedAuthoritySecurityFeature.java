/*******************************************************************************
 * Copyright (C) 2022 the Eclipse BaSyx Authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * SPDX-License-Identifier: MIT
 ******************************************************************************/
package org.eclipse.basyx.components.registry.authorization;

import org.eclipse.basyx.components.configuration.BaSyxSecurityConfiguration;
import org.eclipse.basyx.extensions.aas.directory.tagged.authorized.internal.GrantedAuthorityTaggedDirectoryAuthorizer;
import org.eclipse.basyx.extensions.aas.registration.authorization.internal.GrantedAuthorityAASRegistryAuthorizer;
import org.eclipse.basyx.extensions.shared.authorization.internal.IGrantedAuthorityAuthenticator;
import org.eclipse.basyx.extensions.shared.authorization.internal.ISubjectInformationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specialization of {@link SecurityFeature} for the GrantedAuthority authorization scheme.
 *
 * @author wege
 */
public class GrantedAuthoritySecurityFeature extends SecurityFeature {
  private static Logger logger = LoggerFactory.getLogger(GrantedAuthoritySecurityFeature.class);

  public GrantedAuthoritySecurityFeature(final BaSyxSecurityConfiguration securityConfig) {
    super(securityConfig);
  }

  @Override
  public <SubjectInformationType> AuthorizedRegistryDecorator<SubjectInformationType> getDecorator() {
    logger.info("use GrantedAuthority authorization strategy");
    final ISubjectInformationProvider<SubjectInformationType> subjectInformationProvider = getGrantedAuthoritySubjectInformationProvider();
    final IGrantedAuthorityAuthenticator<SubjectInformationType> grantedAuthorityAuthenticator = getGrantedAuthorityAuthenticator();

    return new AuthorizedRegistryDecorator<>(
        new GrantedAuthorityAASRegistryAuthorizer<>(grantedAuthorityAuthenticator),
        new GrantedAuthorityTaggedDirectoryAuthorizer<>(grantedAuthorityAuthenticator),
        subjectInformationProvider
    );
  }

  @SuppressWarnings("unchecked")
  private <SubjectInformationType> IGrantedAuthorityAuthenticator<SubjectInformationType> getGrantedAuthorityAuthenticator() {
    return securityConfig.loadInstanceDynamically(BaSyxSecurityConfiguration.AUTHORIZATION_STRATEGY_GRANTEDAUTHORITY_GRANTED_AUTHORITY_GRANTED_AUTHORITY_AUTHENTICATOR, IGrantedAuthorityAuthenticator.class);
  }

  @SuppressWarnings("unchecked")
  private <SubjectInformationType> ISubjectInformationProvider<SubjectInformationType> getGrantedAuthoritySubjectInformationProvider() {
    return securityConfig.loadInstanceDynamically(BaSyxSecurityConfiguration.AUTHORIZATION_STRATEGY_GRANTEDAUTHORITY_SUBJECT_INFORMATION_PROVIDER, ISubjectInformationProvider.class);
  }
}
