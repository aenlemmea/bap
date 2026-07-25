export enum BookingStatus {
  PENDING = 'pending',
  CONFIRMED = 'confirmed',
  CANCELLED = 'cancelled',
  REJECTED = 'rejected',
}


export interface CreateBookingRequest {
  listingId: number,
  checkInDate: string,
  checkOutDate: string,
}

export interface BookingResponse {
  id: number,
  listingId: number,
  checkInDate: string,
  checkOutDate: string,
  totalPrice: number,
  status: BookingStatus,
}
