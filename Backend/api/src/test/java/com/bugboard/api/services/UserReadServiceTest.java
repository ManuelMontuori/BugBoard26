package com.bugboard.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        UserReadService userService;

        @Test
        void testGetMonthlyReportValid() {

                int year = 2024;
                int month = 5;

                LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime endDate = startDate.plusMonths(1);

                UserReportProjection projection = mock(UserReportProjection.class);
                UserReportDTO dto = mock(UserReportDTO.class);

                when(userRepository.getUserReports(startDate, endDate))
                                .thenReturn(List.of(projection));

                when(userReportMapper.mapToDTO(projection))
                                .thenReturn(dto);

                List<UserReportDTO> result = userService.getMonthlyReport(year, month);

                assertEquals(1, result.size());
                assertEquals(dto, result.get(0));

                verify(userRepository).getUserReports(startDate, endDate);
                verify(userReportMapper).mapToDTO(projection);
        }

        @Test
        void testGetMonthlyReportEmptyList() {

                int year = 2024;
                int month = 5;

                LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime endDate = startDate.plusMonths(1);

                when(userRepository.getUserReports(startDate, endDate))
                                .thenReturn(List.of());

                List<UserReportDTO> result = userService.getMonthlyReport(year, month);

                assertTrue(result.isEmpty());

                verify(userRepository).getUserReports(startDate, endDate);
                verify(userReportMapper, never()).mapToDTO(any());
        }

        // Boudary test

        @Test
        void testGetMonthlyReportInvalidMonthUpperBound() {

                assertThrows(DateTimeException.class,
                                () -> userService.getMonthlyReport(2024, 13));
        }

        @Test
        void testGetMonthlyReportInvalidMonthLowerBound() {

                assertThrows(DateTimeException.class,
                                () -> userService.getMonthlyReport(2024, 0));
        }

        @Test
        void testGetMonthlyReportBoundaryMinMonth() {

                int year = 2024;
                int month = 1;

                LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime endDate = startDate.plusMonths(1);

                when(userRepository.getUserReports(startDate, endDate))
                                .thenReturn(List.of());

                List<UserReportDTO> result = userService.getMonthlyReport(year, month);

                assertTrue(result.isEmpty());

                verify(userRepository).getUserReports(startDate, endDate);
        }

        @Test
        void testGetMonthlyReportBoundaryMaxMonth() {

                int year = 2024;
                int month = 12;

                LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
                LocalDateTime endDate = startDate.plusMonths(1);

                when(userRepository.getUserReports(startDate, endDate))
                                .thenReturn(List.of());

                List<UserReportDTO> result = userService.getMonthlyReport(year, month);

                assertTrue(result.isEmpty());

                verify(userRepository).getUserReports(startDate, endDate);
        }
}