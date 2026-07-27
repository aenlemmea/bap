/* Response from backend */
export interface ListingResponse {
  id: number;
  title: string;
  description: string;
  available: boolean;
  pricePerNight: number;
  location: string;
  createdAt: string; // LocalDateTime comes as ISO string from backend
  maxGuests: number;
  imageUrls: PropertyImageResponse[];
}

/* Generic Spring Data Page response */
export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number; // Current page (0-based)
  numberOfElements: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

/* Request sent to backend when creating a listing */
export interface CreateListingRequest {
  title: string;
  description?: string;
  pricePerNight: number;
  location: string;
  maxGuests: number;
}

/* Based on PropertyImageResponseDTO */
export interface PropertyImageResponse {
  id: number;
  listingId: number;
  filename: string;
  fileUrl: string;
  primaryImage: boolean;
}
