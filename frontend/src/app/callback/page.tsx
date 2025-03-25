"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

export default function SignInCallbackPage() {
  const router = useRouter();

  useEffect(() => {
    router.push("/");
  }, [router]);
  return <div>Signing in...</div>;
}
