package scalatron.botwar.botPlugin

class ControlFunctionFactory {
    def create : (String => String) = new Bot().respond _
}

class Bot() {
    def respond(input: String): String = ""
}
