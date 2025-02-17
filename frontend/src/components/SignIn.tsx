"use client";

import { signIn, signOut, useSession } from "next-auth/react";

export default function SignIn() {
  const { data: session } = useSession();

  if (session) {
    return (
      <div>
        <p>Access Token: {session.accessToken}</p>
        <br />
        <hr />
        <br />
        <p>Refresh Token: {session.refreshToken}</p>
        <br />
        <button onClick={() => signOut()}>Sign Out</button>
      </div>
    );
  }
  return <button onClick={() => signIn("keycloak")}>Sign In</button>;
}
