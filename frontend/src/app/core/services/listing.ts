import {inject, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CreateListingRequest, ListingResponse} from '../../shared/models';
import {Observable} from 'rxjs';

@Service()
export class Listing {
  private readonly http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/v1/listings';

  getAllListings(): Observable<ListingResponse[]> {
    return this.http.get<ListingResponse[]>(this.API_URL);
  }

  getListingById(id: number): Observable<ListingResponse> {
    return this.http.get<ListingResponse>(`${this.API_URL}/${id}`);
  }

  createListing(data: CreateListingRequest): Observable<ListingResponse> {
    return this.http.post<ListingResponse>(this.API_URL, data);
  }
}
