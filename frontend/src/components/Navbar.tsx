"use client";

import { DisclosureButton } from "@headlessui/react";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useAuth } from "react-oidc-context";

const navigation = [
  { name: "Homepage", href: "/" },
  { name: "Chats", href: "#" },
  { name: "Projects", href: "#" },
  { name: "Calendar", href: "#" },
  { name: "Reports", href: "#" },
];

function classNames(...classes: any) {
  return classes.filter(Boolean).join(" ");
}

export default function Navbar() {
  const path = usePathname();
  const { isAuthenticated } = useAuth();

  return (
    <div className="ml-10 flex items-baseline space-x-4">
      {navigation.map((item) => (
        <Link
          key={item.name}
          href={isAuthenticated ? item.href : "/"}
          aria-current={item.href == path ? "page" : undefined}
          className={classNames(
            item.href == path
              ? "bg-gray-900 text-white"
              : "text-gray-300 hover:bg-gray-700 hover:text-white",
            "rounded-md px-3 py-2 text-sm font-medium"
          )}
        >
          {item.name}
        </Link>
      ))}
    </div>
  );
}

export function MobileNavbar() {
  const path = usePathname();
  const { isAuthenticated } = useAuth();

  return (
    <div className="space-y-1 px-2 pt-2 pb-3 sm:px-3">
      {navigation.map((item) => (
        <DisclosureButton
          key={item.name}
          as="a"
          href={isAuthenticated ? item.href : "/"}
          aria-current={item.href == path ? "page" : undefined}
          className={classNames(
            item.href == path
              ? "bg-gray-900 text-white"
              : "text-gray-300 hover:bg-gray-700 hover:text-white",
            "block rounded-md px-3 py-2 text-base font-medium"
          )}
        >
          {item.name}
        </DisclosureButton>
      ))}
    </div>
  );
}
