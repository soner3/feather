import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface TokenState {
  accessToken: string;
}

const initialTokenState: TokenState = {
  accessToken: "",
};

export const tokenSlice = createSlice({
  name: "token",
  initialState: initialTokenState,
  reducers: {
    setAccessToken: (state, action: PayloadAction<string>) => {
      state.accessToken = action.payload;
    },
  },
});

export const { setAccessToken } = tokenSlice.actions;
export default tokenSlice.reducer;
