"use client";

import { useAuth } from "react-oidc-context";
import useStompClient from "./useStompClient";

export default function SignIn() {
  const auth = useAuth();
  useStompClient(auth.isAuthenticated);

  switch (auth.activeNavigator) {
    case "signinSilent":
      return <div>Signing you in...</div>;
    case "signoutRedirect":
      return <div>Signing you out...</div>;
  }

  if (auth.isLoading) {
    return <div>Loading...</div>;
  }

  if (auth.error) {
    return <div>Oops... {auth.error.message}</div>;
  }

  if (auth.isLoading) {
    return <div>Signing you in/out...</div>;
  }

  if (auth.isAuthenticated) {
    return (
      <>
        <p>Access Token: {auth.user?.access_token}</p>
        <div>
          <br />
          <p>Hello {auth.user?.profile.sub}</p>
          <br />
          <button
            onClick={async () => {
              const res = await fetch("http://localhost:9000/v1/user", {
                headers: {
                  "Content-Type": "application/json",
                },
                credentials: "include",
              });
              const data = await res.json();
              console.log(data);
            }}
          >
            Fetch User
          </button>
          <br />
          <button onClick={() => auth.signinSilent()}>Refresh</button>
          <br />
          <button onClick={() => auth.signoutRedirect()}>Log out</button>
        </div>
      </>
    );
  }

  return <button onClick={() => auth.signinRedirect()}>Log in</button>;
}
