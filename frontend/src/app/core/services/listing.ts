import {inject, Injectable, Service} from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import {
  CreateListingRequest,
  ListingResponse,
  PageResponse,
} from '../../shared/models';
import { Observable } from 'rxjs';

@Service()
export class Listing {
  private readonly http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8080/api/v1/listings';

  getAllListings(
    page = 0,
    size = 10,
    sort = 'createdAt,desc'
  ): Observable<PageResponse<ListingResponse>> {
    const params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    return this.http.get<PageResponse<ListingResponse>>(this.API_URL, {
      params,
    });
  }

  getListingById(id: number): Observable<ListingResponse> {
    return this.http.get<ListingResponse>(`${this.API_URL}/${id}`);
  }

  createListing(data: CreateListingRequest): Observable<ListingResponse> {
    return this.http.post<ListingResponse>(this.API_URL, data);
  }
}
