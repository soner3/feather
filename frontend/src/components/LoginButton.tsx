"use client";

import { useAuth } from "react-oidc-context";
import Button from "./Button";

export default function LoginButton() {
  const { signinRedirect, isAuthenticated, signoutRedirect } = useAuth();
  return (
    <Button
      onClick={
        isAuthenticated ? () => signoutRedirect() : () => signinRedirect()
      }
    >
      {isAuthenticated ? "Sign out" : "Sign in"}
    </Button>
  );
}
