import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Listing } from '../../../core/services/listing';
import { ListingResponse } from '../../../shared/models';

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
  isLoading = signal(true);
  errorMessage = signal<string | null>(null);

  // Pagination
  page = signal(0);
  size = 10;
  totalPages = signal(0);
  totalElements = signal(0);

  readonly imageBaseUrl = 'http://localhost:8080';

  ngOnInit(): void {
    this.loadListings();
  }

  loadListings(): void {
    this.isLoading.set(true);
    this.errorMessage.set(null);

    this.listingService
      .getAllListings(this.page(), this.size)
      .subscribe({
        next: (response) => {
          this.listings.set(response.content);
          this.totalPages.set(response.totalPages);
          this.totalElements.set(response.totalElements);
          this.isLoading.set(false);
        },
        error: () => {
          this.errorMessage.set(
            'Failed to load listings. Please try again later.'
          );
          this.isLoading.set(false);
        },
      });
  }

  previousPage(): void {
    if (this.page() > 0) {
      this.page.update((p) => p - 1);
      this.loadListings();
    }
  }

  nextPage(): void {
    if (this.page() < this.totalPages() - 1) {
      this.page.update((p) => p + 1);
      this.loadListings();
    }
  }

  getImageUrl(path: string): string {
    return `${this.imageBaseUrl}${path}`;
  }
}
