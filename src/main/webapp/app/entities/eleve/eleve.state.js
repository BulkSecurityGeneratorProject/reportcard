(function() {
    'use strict';

    angular
        .module('reportcardApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('eleve', {
            parent: 'entity',
            url: '/eleve?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Eleves'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/eleve/eleves.html',
                    controller: 'EleveController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('eleve-detail', {
            parent: 'entity',
            url: '/eleve/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Eleve'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/eleve/eleve-detail.html',
                    controller: 'EleveDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Eleve', function($stateParams, Eleve) {
                    return Eleve.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'eleve',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('eleve-detail.edit', {
            parent: 'eleve-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-dialog.html',
                    controller: 'EleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Eleve', function(Eleve) {
                            return Eleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('eleve.new', {
            parent: 'eleve',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-dialog.html',
                    controller: 'EleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                matricule: null,
                                nom: null,
                                prenom: null,
                                datenaissance: null,
                                lieunaissance: null,
                                sexe: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('eleve', null, { reload: 'eleve' });
                }, function() {
                    $state.go('eleve');
                });
            }]
        })
        .state('eleve.edit', {
            parent: 'eleve',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-dialog.html',
                    controller: 'EleveDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Eleve', function(Eleve) {
                            return Eleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('eleve', null, { reload: 'eleve' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('eleve.delete', {
            parent: 'eleve',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/eleve/eleve-delete-dialog.html',
                    controller: 'EleveDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Eleve', function(Eleve) {
                            return Eleve.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('eleve', null, { reload: 'eleve' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
