import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from '@angular/forms';
import { Listing } from '../../../core/services/listing';
import { Booking } from '../../../core/services/booking';
import { AuthService } from '../../../core/services/auth.service';
import {
  CreateBookingRequest,
  ListingResponse,
} from '../../../shared/models';

@Component({
  selector: 'app-listing-detail',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './listing-detail.html',
  styleUrl: './listing-detail.css',
})
export class ListingDetailComponent implements OnInit {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly fb = inject(FormBuilder);
  private readonly listingService = inject(Listing);
  private readonly bookingService = inject(Booking);

  readonly authService = inject(AuthService);

  listing = signal<ListingResponse | null>(null);
  isLoading = signal(true);
  isSubmitting = signal(false);

  bookingSuccess = signal(false);
  bookingError = signal<string | null>(null);

  readonly imageBaseUrl = 'http://localhost:8080';
  readonly today = new Date().toISOString().split('T')[0];

  bookingForm = this.fb.group(
    {
      checkInDate: ['', Validators.required],
      checkOutDate: ['', Validators.required],
      guestCount: [
        1,
        [
          Validators.required,
          Validators.min(1),
        ],
      ],
    },
    {
      validators: this.dateRangeValidator(),
    }
  );

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.router.navigate(['/']);
      return;
    }

    const id = Number(idParam);

    if (Number.isNaN(id)) {
      this.router.navigate(['/']);
      return;
    }

    this.listingService.getListingById(id).subscribe({
      next: (data) => {
        this.listing.set(data);

        this.bookingForm.controls.guestCount.addValidators(
          Validators.max(data.maxGuests)
        );

        this.bookingForm.controls.guestCount.updateValueAndValidity();

        this.isLoading.set(false);
      },
      error: () => {
        this.router.navigate(['/']);
      },
    });
  }

  onBook(): void {
    if (
      this.bookingForm.invalid ||
      !this.listing() ||
      this.isSubmitting()
    ) {
      return;
    }

    this.isSubmitting.set(true);

    const payload: CreateBookingRequest = {
      listingId: this.listing()!.id,
      checkInDate: this.bookingForm.value.checkInDate!,
      checkOutDate: this.bookingForm.value.checkOutDate!,
      guestCount: Number(this.bookingForm.value.guestCount),
    };

    this.bookingService.createBooking(payload).subscribe({
      next: () => {
        this.bookingSuccess.set(true);
        this.bookingError.set(null);

        this.bookingForm.reset({
          checkInDate: '',
          checkOutDate: '',
          guestCount: 1,
        });

        this.isSubmitting.set(false);
      },
      error: (err) => {
        this.bookingSuccess.set(false);
        this.bookingError.set(
          err.error?.message ?? 'Failed to complete booking.'
        );

        this.isSubmitting.set(false);
      },
    });
  }

  getImageUrl(path: string): string {
    return `${this.imageBaseUrl}${path}`;
  }

  private dateRangeValidator(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const checkIn = control.get('checkInDate')?.value;
      const checkOut = control.get('checkOutDate')?.value;

      if (!checkIn || !checkOut) {
        return null;
      }

      return new Date(checkOut) > new Date(checkIn)
        ? null
        : { invalidDateRange: true };
    };
  }
}
