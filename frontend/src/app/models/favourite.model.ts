export interface Favourite {
  id: number;
  userId: number;
  tcodeId: number;
  sortOrder: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateFavouriteRequest {
  userId: number;
  tcodeId: number;
  sortOrder: number;
}
