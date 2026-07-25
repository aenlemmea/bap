import {inject, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BookingResponse, CreateBookingRequest} from '../../shared/models';
import {Observable} from 'rxjs';

@Service()
export class Booking {
  private readonly http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/v1/bookings';

  createBooking(data: CreateBookingRequest): Observable<BookingResponse> {
    return this.http.post<BookingResponse>(this.API_URL, data);
  }

  getUserBookings(): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${this.API_URL}/my-bookings`);
  }
}
