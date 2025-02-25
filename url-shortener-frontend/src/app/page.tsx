import ShortenForm from "@/components/ShortenForm";

export default function Home() {
  return (
      <main className="flex min-h-screen flex-col items-center justify-center p-10">
        <h1 className="text-2xl font-bold">Scalable URL Shortener</h1>
        <ShortenForm />
      </main>
  );
}
