package gerbarika.web.application.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Controller
class Controller {

    @GetMapping("/")
    fun mainDefault(): String = "redirect:/graph/income"

    @GetMapping("/list")
    fun main(): String = "list"

    @GetMapping("/income")
    fun income(): String = "income"

    @GetMapping("/purchases")
    fun regionPurchasesInfo(
        @RequestParam region: String,
        @RequestParam before: LocalDateTime,
        @RequestParam after: LocalDateTime
    ): String = "region"

    @GetMapping("/list/search")
    fun sortedList(): String = "sortedList"

    @GetMapping("/login")
    fun login(): String = "login"

    @GetMapping("/graph/income")
    fun graph(): String = "graph"

    @GetMapping("/graph/item")
    fun itemGraph(): String = "itemGraph"
}