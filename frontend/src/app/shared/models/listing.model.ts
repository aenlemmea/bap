export interface ListingResponse {
  id: number,
  title: string,
  pricePerNight: number,
}

export interface CreateListingRequest {
  title: string,
  pricePerNight: number,
  available: boolean,
}
