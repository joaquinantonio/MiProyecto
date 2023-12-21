package com.ja.miproyecto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminDoctorDetailsTest {

    @Mock
    private DatabaseReference mockReferenceDoctor;

    @Mock
    private DatabaseReference mockReferenceBooking;

    @InjectMocks
    private AdminDoctorDetails adminDoctorDetails;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnCreate() {
        // Mock data
        DataSnapshot mockDataSnapshot = mock(DataSnapshot.class);
        when(mockDataSnapshot.exists()).thenReturn(true);
        when(mockDataSnapshot.child(anyString())).thenReturn(mock(DataSnapshot.class));

        // Mock Firebase calls
        when(mockReferenceDoctor.child(anyString())).thenReturn(mockReferenceDoctor);
        when(mockReferenceDoctor.addValueEventListener(any())).thenAnswer(invocation -> {
            ValueEventListener eventListener = invocation.getArgument(0);
            eventListener.onDataChange(mockDataSnapshot);
            return null;
        });

        when(mockReferenceBooking.child(anyString())).thenReturn(mockReferenceBooking);
        when(mockReferenceBooking.child(anyString()).addValueEventListener(any())).thenAnswer(invocation -> {
            ValueEventListener eventListener = invocation.getArgument(0);
            eventListener.onDataChange(mockDataSnapshot);
            return null;
        });

        // Call the method to be tested
        adminDoctorDetails.onCreate(null);

        // Verify that the UI components are updated correctly

        verify(adminDoctorDetails.doctor_name).setText(anyString());
        verify(adminDoctorDetails.doctor_spec).setText(anyString());
        verify(adminDoctorDetails.doctor_about).setText(anyString());

        // Verify Firebase calls
        verify(mockReferenceDoctor, times(1)).child(anyString());
        verify(mockReferenceBooking, times(1)).child(anyString());
    }
}

