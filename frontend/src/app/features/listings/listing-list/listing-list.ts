import {Component, inject, OnInit, signal} from '@angular/core';
import {RouterLink} from '@angular/router';
import { Listing } from '../../../core/services/listing';
import {ListingResponse} from '../../../shared/models';

@Component({
  selector: 'app-listing-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './listing-list.html',
  styleUrl: './listing-list.css',
})
export class ListingList implements OnInit {
  private readonly listingService = inject(Listing);

  listings = signal<ListingResponse[]>([]);
  isLoading = signal<boolean>(true);
  errorMessage = signal<string | null>(null);

  ngOnInit(): void {
    this.listingService.getAllListings().subscribe({
      next: (data) => {
        this.listings.set(data);
        this.isLoading.set(false);
      },
      error: (err) => {
        this.errorMessage.set('Failed to load listings. Please try again later.');
        this.isLoading.set(false);
      }
    });
  }
}
