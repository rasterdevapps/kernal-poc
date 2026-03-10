export interface RecentNavigation {
  id: number;
  userId: number;
  tcodeId: number;
  accessedAt: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateRecentNavigationRequest {
  userId: number;
  tcodeId: number;
}
