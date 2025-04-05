import Button from "../components/Button";
import LoginButton from "../components/LoginButton";

export default function HomePage() {
  return (
    <section className="flex-col mt-8 h-full w-full justify-center items-center">
      <h2 className="font-bold text-center text-4xl">Welcome to Feather</h2>
      <p className="text-xl text-center mt-4">
        A free to use realtime chat application for everyone. Try it out.
      </p>
      <div className="flex justify-center gap-20 w-full mt-8 items-center">
        <LoginButton />
        <Button>Register</Button>
      </div>
    </section>
  );
}
