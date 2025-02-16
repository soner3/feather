import { configureStore } from "@reduxjs/toolkit";
import tokenReducer from "./features/token/tokenSlice";

export const makeStore = () => {
  return configureStore({
    reducer: {
      token: tokenReducer,
    },
  });
};

export type AppStore = ReturnType<typeof makeStore>;
export type RootState = ReturnType<AppStore["getState"]>;
export type AppDispatch = AppStore["dispatch"];
