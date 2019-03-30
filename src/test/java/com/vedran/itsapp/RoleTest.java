package com.vedran.itsapp;


import com.vedran.itsapp.model.embedded.Role;
import org.junit.Test;

import java.util.*;
import java.util.function.BiPredicate;

public class RoleTest {
  @Test
  public void testRole(){
    Set<Role> changer = new HashSet<>(Arrays.asList(Role.ROLE_USER_ADMINISTRATOR,
            Role.ROLE_USER_ADMINISTRATOR,Role.ROLE_PRODUCT_ADMINISTRATOR,Role.ROLE_USER_EMPLOYEE));

    Set<Role> subject = new HashSet<>(Arrays.asList(
            Role.ROLE_PRODUCT_ADMINISTRATOR,Role.ROLE_USER_EMPLOYEE));

    OptionalInt max1 = changer.stream().mapToInt(r -> r.ordinal()).min();
    OptionalInt max2 = subject.stream().mapToInt(r -> r.ordinal()).min();

    BiPredicate<Integer , Integer> canChange = (c, s) -> c < s;

    boolean can = canChange.and((c1,c2) -> !c1.equals(c2)).test(max1.getAsInt(),max2.getAsInt());
    System.out.println(can);
  }
}
