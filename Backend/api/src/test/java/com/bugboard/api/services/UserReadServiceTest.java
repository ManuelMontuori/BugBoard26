package com.bugboard.api.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.mapper.UserReportMapper;
import com.bugboard.api.repositories.UserRepository;
import com.bugboard.api.repositories.projection.UserReportProjection;

@ExtendWith(MockitoExtension.class)
public class UserReadServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserReportMapper userReportMapper;

    @InjectMocks
    UserService userService;

//test per il metodo getMonthlyReport, input valido, deve restituire lista di UserReportDTO
    @Test
    void testGetMonthlyReport_valid() {

    int year = 2024;
    int month = 5;

    LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime endDate = startDate.plusMonths(1);

    // ✅ tipo corretto
    UserReportProjection projection = mock(UserReportProjection.class);
    UserReportDTO dto = mock(UserReportDTO.class);

    List<UserReportProjection> projections = List.of(projection);

    // mock repository
    when(userRepository.getUserReports(startDate, endDate))
            .thenReturn(projections);

    // mock mapper
    when(userReportMapper.mapToDTO(projection))
            .thenReturn(dto);

    // esecuzione
    List<UserReportDTO> result =
            userService.getMonthlyReport(year, month);

    // ✅ assert
    assertEquals(1, result.size());
    assertEquals(dto, result.get(0));

    // ✅ verify
    verify(userRepository).getUserReports(startDate, endDate);
    verify(userReportMapper).mapToDTO(projection);
}

//input valido, ma non ci sono utenti nel database, deve restituire lista vuota
@Test
void testGetMonthlyReport_emptyList() {

    int year = 2024;
    int month = 5;

    LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
    LocalDateTime endDate = startDate.plusMonths(1);

    when(userRepository.getUserReports(startDate, endDate))
            .thenReturn(List.of());

    List<UserReportDTO> result =
            userService.getMonthlyReport(year, month);

    assertTrue(result.isEmpty());

    verify(userRepository).getUserReports(startDate, endDate);
}

//input non valido, ad esempio mese 13, deve lanciare eccezione
@Test
void testGetMonthlyReport_invalidMonth() {

    assertThrows(DateTimeException.class, () -> {
        userService.getMonthlyReport(2024, 13);
    });
}





}
