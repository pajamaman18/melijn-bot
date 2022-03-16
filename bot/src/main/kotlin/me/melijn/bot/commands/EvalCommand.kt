package me.melijn.bot.commands

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.chatCommand
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.behavior.edit
import me.melijn.bot.utils.CodeEvalUtil
import me.melijn.bot.utils.KordExUtils.userIsOwner

class EvalCommand : Extension() {

    override val name: String = "eval"

    override suspend fun setup() {
        chatCommand(::EvalArgs) {
            name = this@EvalCommand.name
            description = "evaluating code"
            check {
                userIsOwner()
            }

            action {
                val msg = this.channel.createMessage {
                    this.content = "Executing code.."
                }

                val paramStr = "context: ChatCommandContext<out EvalCommand.EvalArgs>"
                val result = CodeEvalUtil.runCode(this.argString, paramStr, this)

                msg.edit {
                    this.content = "Done!\nResult: $result"
                }
            }
        }
    }

    inner class EvalArgs : Arguments() {
        val code = string {
            this.name = "code"
            this.description = "code to execute"
        }
    }
}