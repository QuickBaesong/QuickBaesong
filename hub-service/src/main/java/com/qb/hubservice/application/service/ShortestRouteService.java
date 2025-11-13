package com.qb.hubservice.application.service;

import com.qb.hubservice.domain.model.HubRoute;
import com.qb.hubservice.domain.repository.HubRouteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service // 서비스 로직이므로 @Service를 추가하는 것이 좋습니다.
public class ShortestRouteService {

    private final HubRouteRepository hubRouteRepository;

    /**
     * 두 허브 사이의 최단 경로를 다익스트라 알고리즘으로 계산
     */
    public ShortestPathResult findShortestPath(UUID startHubId, UUID endHubId) {
        // 그래프 구축
        Map<UUID, List<Edge>> graph = buildGraph();

        // 다익스트라 알고리즘 실행
        return dijkstra(graph, startHubId, endHubId);
    }


    private Map<UUID, List<Edge>> buildGraph() {
        Map<UUID, List<Edge>> graph = new HashMap<>();
        List<HubRoute> allRoutes = hubRouteRepository.findAll();

        for (HubRoute route : allRoutes) {
            UUID startHub = route.getStartHub().getHubId();
            UUID destHub = route.getDestinationHub().getHubId();

            Edge edge = new Edge(destHub, route.getDistance(), route.getDuration());

            // 양방향 그래프 (필요시 단방향으로 변경)
            graph.computeIfAbsent(startHub, k -> new ArrayList<>()).add(edge);

            // 역방향도 추가 (양방향 경로 지원)
            Edge reverseEdge = new Edge(startHub, route.getDistance(), route.getDuration());
            graph.computeIfAbsent(destHub, k -> new ArrayList<>()).add(reverseEdge);
        }

        return graph;
    }


    private ShortestPathResult dijkstra(Map<UUID, List<Edge>> graph, UUID start, UUID end) {
        // 1. 초기 무한대 설정 (BigDecimal.valueOf(Double.MAX_VALUE) 사용을 피함)
        final BigDecimal INFINITY_DISTANCE = new BigDecimal("99999999999.000");
        final long INFINITY_DURATION = Long.MAX_VALUE;

        Map<UUID, BigDecimal> distances = new HashMap<>();
        // 2. 최단 시간 추적용 맵 추가
        Map<UUID, Long> durations = new HashMap<>();

        Map<UUID, UUID> previous = new HashMap<>();
        // PQ는 거리(distance) 기준으로 정렬합니다.
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparing(Node::getDistance));
        Set<UUID> visited = new HashSet<>();

        // 초기화
        for (UUID node : graph.keySet()) {
            distances.put(node, INFINITY_DISTANCE);
            durations.put(node, INFINITY_DURATION); // 맵 초기화
        }
        distances.put(start, BigDecimal.ZERO);
        durations.put(start, 0L); // 시작 노드 시간 0으로 설정

        // Node 객체는 거리와 시간을 모두 가집니다.
        pq.offer(new Node(start, BigDecimal.ZERO, 0L));

        // 다익스트라 알고리즘
        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (visited.contains(current.getId())) {
                continue;
            }
            visited.add(current.getId());

            if (current.getId().equals(end)) {
                break;
            }

            List<Edge> neighbors = graph.getOrDefault(current.getId(), new ArrayList<>());
            for (Edge edge : neighbors) {
                UUID neighbor = edge.getDestinationHubId();
                BigDecimal newDistance = current.getDistance().add(edge.getDistance());
                // 3. 인접 노드의 새로운 시간 계산
                Long newDuration = current.getDuration() + edge.getDuration();

                // 최단 거리가 갱신되는 경우
                if (newDistance.compareTo(distances.get(neighbor)) < 0) {
                    distances.put(neighbor, newDistance);
                    durations.put(neighbor, newDuration); // 최단 시간도 함께 갱신
                    previous.put(neighbor, current.getId());
                    // PQ에 새로운 Node (거리와 시간이 모두 업데이트된)를 추가합니다.
                    pq.offer(new Node(neighbor, newDistance, newDuration));
                }
                // (선택적) 만약 거리가 같고, 시간이 더 짧다면 갱신할 수도 있지만,
                // 현재 로직은 '최단 거리' 기준이므로 이 로직은 생략합니다.
            }
        }

        // 경로 재구성
        List<UUID> path = new ArrayList<>();
        UUID current = end;

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        if (path.isEmpty() || !path.get(0).equals(start)) {
            log.warn("경로를 찾을 수 없습니다. Start: {}, End: {}", start, end);
            return ShortestPathResult.notFound();
        }

        // 4. 최단 거리와 최단 시간을 모두 반환합니다.
        Long totalDuration = durations.get(end).equals(INFINITY_DURATION) ? 0L : durations.get(end);

        return ShortestPathResult.success(path, distances.get(end), totalDuration);
    }
}