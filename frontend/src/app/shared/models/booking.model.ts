/* Request sent to backend when creating a booking */
export interface CreateBookingRequest {
  listingId: number;
  checkInDate: string;   // LocalDate -> ISO string (YYYY-MM-DD)
  checkOutDate: string;  // LocalDate -> ISO string (YYYY-MM-DD)
  guestCount: number;
}

/* Response received from backend */
export interface BookingResponse {
  id: number;
  listingId: number;
  checkInDate: string;   // LocalDate -> ISO string
  checkOutDate: string;  // LocalDate -> ISO string
  totalPrice: number;    // BigDecimal -> number
  guestCount: number;
  status: BookingStatus;
}

/* Enum matching backend BookingStatus */
export enum BookingStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  CANCELLED = 'CANCELLED',
  REJECTED = 'REJECTED'
}
