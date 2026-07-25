import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Listing } from '../../../core/services/listing';
import { Booking } from '../../../core/services/booking';
import { AuthService } from '../../../core/services/auth.service';
import { ListingResponse, CreateBookingRequest } from '../../../shared/models';

@Component({
  selector: 'app-listing-detail',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './listing-detail.html',
  styleUrl: './listing-detail.css'
})
export class ListingDetailComponent implements OnInit {

  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly listingService = inject(Listing);
  private readonly bookingService = inject(Booking);
  readonly authService = inject(AuthService);

  listing = signal<ListingResponse | null>(null);
  isLoading = signal<boolean>(true);

  bookingSuccess = signal<boolean>(false);
  bookingError = signal<string | null>(null);


  bookingForm = this.fb.group({
    checkInDate: ['', Validators.required],
    checkOutDate: ['', Validators.required],
    guestCount: [
      1,
      [
        Validators.required,
        Validators.min(1)
      ]
    ]
  });


  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (idParam) {
      const id = Number(idParam);

      this.listingService.getListingById(id).subscribe({
        next: (data) => {
          this.listing.set(data);
          this.isLoading.set(false);
        },
        error: () => {
          this.isLoading.set(false);
        }
      });
    }
  }


  onBook(): void {

    if (this.bookingForm.invalid || !this.listing()) {
      return;
    }

    const payload: CreateBookingRequest = {
      listingId: this.listing()!.id,
      checkInDate: this.bookingForm.value.checkInDate!,
      checkOutDate: this.bookingForm.value.checkOutDate!,
      guestCount: Number(this.bookingForm.value.guestCount)
    };


    this.bookingService.createBooking(payload).subscribe({

      next: () => {
        this.bookingSuccess.set(true);
        this.bookingError.set(null);

        this.bookingForm.reset({
          checkInDate: '',
          checkOutDate: '',
          guestCount: 1
        });
      },


      error: (err) => {
        this.bookingSuccess.set(false);
        this.bookingError.set(
          err.error?.message || 'Failed to complete booking.'
        );
      }

    });
  }

  readonly imageBaseUrl = 'http://localhost:8080';

  getImageUrl(path: string): string {
    return `${this.imageBaseUrl}${path}`;
  }
}
