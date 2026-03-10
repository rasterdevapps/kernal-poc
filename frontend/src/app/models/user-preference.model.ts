export interface UserPreference {
  id: number;
  userId: number;
  themeId: number;
  locale: string;
  dateFormat: string;
  timeFormat: string;
  itemsPerPage: number;
  createdAt: string;
  updatedAt: string;
}

export interface CreateUserPreferenceRequest {
  userId: number;
  themeId?: number;
  locale: string;
  dateFormat: string;
  timeFormat: string;
  itemsPerPage: number;
}
