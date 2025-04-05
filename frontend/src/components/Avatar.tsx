"use client";

import { MenuButton } from "@headlessui/react";
import { getRandomImage } from "../lib/randomImage";

const user = {
  name: "Tom Cook",
  email: "tom@example.com",
  imageUrl: null,
};

export default function Avatar() {
  return (
    <MenuButton className="relative flex max-w-xs items-center rounded-full bg-gray-800 text-sm focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800 focus:outline-hidden">
      <span className="absolute -inset-1.5" />
      <span className="sr-only">Open user menu</span>
      <img
        alt="Profile Picture"
        src={user.imageUrl ? user.imageUrl : getRandomImage()}
        className="size-8 rounded-full"
      />
    </MenuButton>
  );
}

export function MobileAvatar() {
  return (
    <div className="flex items-center px-5">
      <div className="shrink-0">
        <img
          alt="Profile Picture"
          src={user.imageUrl ? user.imageUrl : getRandomImage()}
          className="size-10 rounded-full"
        />
      </div>
      <div className="ml-3">
        <div className="text-base/5 font-medium text-white">{user.name}</div>
        <div className="text-sm font-medium text-gray-400">{user.email}</div>
      </div>
    </div>
  );
}
