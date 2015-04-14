package de.is24.aws.instancemetadataserver;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RemoteHostRoleNameProducerTest {

  private final RemoteHostRoleNameProducer remoteHostRoleNameProducer = new RemoteHostRoleNameProducer();

  @Test
  public void dev_rz_host_should_get_correct_role_name() {
    assertThat(calculateRoleNameForHostName("devfoo01.rz.is").get(), is("rz_devfoo"));
  }
  
  @Test
  public void dev_d_rz_host_should_get_correct_role_name() {
    assertThat(calculateRoleNameForHostName("devfoo23.d.rz.is").get(), is("rz_devfoo"));
  }
  
  @Test
  public void tuv_rz_host_should_get_correct_role_name() {
    assertThat(calculateRoleNameForHostName("tuvfoo01.rz.is").get(), is("rz_tuvfoo"));
  }
  
  @Test
  public void ham_rz_host_should_get_correct_role_name() {
    assertThat(calculateRoleNameForHostName("hamfoo01.rz.is").get(), is("rz_profoo"));
  }
  
  @Test
  public void ber_rz_host_should_get_correct_role_name() {
    assertThat(calculateRoleNameForHostName("berfoo01.rz.is").get(), is("rz_profoo"));
  }
  
  @Test
  public void unfit_host_should_get_no_role_name() {
    assertThat(calculateRoleNameForHostName("devfoo.foo.bar").isPresent(), is(false));
  }

  public Optional<String> calculateRoleNameForHostName(String hostname) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRemoteHost(hostname);
    return remoteHostRoleNameProducer.getRoleName(request);
  }

}