"use client";

export default function SignIn() {
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
  return <button onClick={() => signIn()}>Sign In</button>;
}
