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
