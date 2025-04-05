export function getRandomImage(): string {
  const rand = Math.floor(Math.random() * 8 + 1);
  return `logo-${rand}.jpg`;
}
