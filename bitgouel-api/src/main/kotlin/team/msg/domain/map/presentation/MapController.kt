package team.msg.domain.map.presentation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import team.msg.domain.map.presentation.response.GetCoordinateResponse
import team.msg.domain.map.service.MapService

@RestController
@RequestMapping("/map")
class MapController(
    private val mapService: MapService
) {
    @GetMapping
    fun getCoordinate(@RequestParam address: String): ResponseEntity<GetCoordinateResponse> {
        val response = mapService.getCoordinate(address)
        return ResponseEntity.ok(response)
    }
}