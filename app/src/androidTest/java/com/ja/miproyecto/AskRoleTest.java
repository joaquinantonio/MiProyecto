package com.ja.miproyecto;

import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AskRoleTest {
    @Mock
    PatientSessionManagement mockPatientSessionManagement;

    @Mock
    DoctorsSessionManagement mockDoctorsSessionManagement;

    @InjectMocks
    AskRole askRole;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheckPatientSessionLoggedin() {
        when(mockPatientSessionManagement.getSession()).thenReturn("123"); // Replace with a valid session ID

        askRole.checkPatientSession();

        verify(mockPatientSessionManagement).getSession();
        verify(askRole).moveToPatientActivity();
    }

    @Test
    public void testCheckPatientSessionNotLoggedin() {
        when(mockPatientSessionManagement.getSession()).thenReturn("-1");

        askRole.checkPatientSession();

        verify(mockPatientSessionManagement).getSession();
        verify(askRole).startActivity(new Intent(askRole, PatientLogin.class));
    }

    @Test
    public void testCheckDoctorSessionLoggedin() {
        when(mockDoctorsSessionManagement.getDoctorSession()).thenReturn(new String[]{"123", "doctor"}); // Replace with valid data

        askRole.checkDoctorSession();

        verify(mockDoctorsSessionManagement).getDoctorSession();
        verify(askRole).moveToDoctorActivity();
    }

    @Test
    public void testCheckDoctorSessionNotLoggedin() {
        when(mockDoctorsSessionManagement.getDoctorSession()).thenReturn(new String[]{"-1", ""});

        askRole.checkDoctorSession();

        verify(mockDoctorsSessionManagement).getDoctorSession();
        verify(askRole).startActivity(new Intent(askRole, DoctorsLogin.class));
    }

    @Test
    public void testMoveToDoctorActivityDoctor() {
        when(mockDoctorsSessionManagement.getDoctorSession()).thenReturn(new String[]{"123", "doctor"}); // Replace with valid data

        askRole.moveToDoctorActivity();

        verify(mockDoctorsSessionManagement).getDoctorSession();
        verify(askRole).startActivity(new Intent(askRole, Doctors.class));
    }

    @Test
    public void testMoveToDoctorActivityAdmin() {
        when(mockDoctorsSessionManagement.getDoctorSession()).thenReturn(new String[]{"123", "Admin"}); // Replace with valid data

        askRole.moveToDoctorActivity();

        verify(mockDoctorsSessionManagement).getDoctorSession();
        verify(askRole).startActivity(new Intent(askRole, AdminActivity.class));
    }

    @Test
    public void testMoveToPatientActivity() {
        askRole.moveToPatientActivity();

        verify(askRole).startActivity(new Intent(askRole, Patient.class));
    }
}
